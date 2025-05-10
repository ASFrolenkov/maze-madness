import { useCallback, useEffect, useRef, useState } from "react";
import { GameUI } from "Types/game";
import { Container, GeneralWrapper, RoomButtonsWrapper } from "./styled";
import { Players } from "./Player";
import { SceneNames } from "Constants";
import { Chat } from "UI";
import { PlayerServerResponse } from "Types/player";

// сделать хук useTooltip для отображаения модалки около места клика(для кика игрока)
export const RoomUI: GameUI = ({ game }) => {
  const [players, setPlayers] = useState<string[]>([]);
  const currentUsername = game.game?.registry.get("username");
  const isFirstRender = useRef(true);

  const leaveRoom = useCallback(() => {
    game.scene?.emitSocket(
      "roomMessageSendClient",
      JSON.stringify([null, `${currentUsername} leave`])
    );
    game.scene?.scene.start(SceneNames.Lobbies);
  }, []);

  const startGame = useCallback(() => {
    game.scene?.emitSocket("roomGameStartClient");
  }, []);

  useEffect(() => {
    const serverResponseCallback = (response: string[]) => {
      if (response.includes(currentUsername)) return setPlayers(response);
      leaveRoom();
    };
    const startGameCallback = (response: string) => {
      const players = JSON.parse(response) as PlayerServerResponse;
      game.game?.registry.set("players", players);
      game.scene?.scene.start(SceneNames.Game);
    };

    if (isFirstRender.current) {
      game.scene?.emitSocket("roomGetUsersClient");
      isFirstRender.current = false;
    }
    game.scene?.addSocketListener("roomGetUsersServer", serverResponseCallback);
    game.scene?.addSocketListener(
      "roomUserLeaveServer",
      serverResponseCallback
    );
    game.scene?.addSocketListener("roomHostLeaveServer", leaveRoom);
    // вызывает scene_lobby is ready 2 раза
    game.scene?.addSocketListener("disconnect", leaveRoom);
    game.scene?.addSocketListener("roomGameStartServer", startGameCallback);

    return () => {
      game.scene?.removeSocketListener(
        "roomGetUsersServer",
        serverResponseCallback
      );
      game.scene?.removeSocketListener(
        "roomUserLeaveServer",
        serverResponseCallback
      );
      game.scene?.removeSocketListener("roomHostLeaveServer", leaveRoom);
      game.scene?.removeSocketListener("disconnect", leaveRoom);
      game.scene?.removeSocketListener(
        "roomGameStartServer",
        startGameCallback
      );
    };
  }, []);

  const kickPlayer = useCallback((playerName: string) => {
    game.scene?.emitSocket(
      "roomMessageSendClient",
      JSON.stringify([null, `${playerName} leave`])
    );
    game.scene?.emitSocket("roomUserKickClient", playerName);
  }, []);

  const isCantStartGame = players.length < 2 || players[0] !== currentUsername;

  return (
    <GeneralWrapper>
      <Container>
        {/* Вынести в отдельный компонент, прокинуть в него game. Рендерить визуал через renderProps */}
        <Players
          players={players}
          currentUsername={currentUsername}
          kickPlayerCb={kickPlayer}
        />
        <Chat game={game} />
      </Container>
      <RoomButtonsWrapper>
        <button onClick={leaveRoom}>leave</button>
        <button onClick={startGame} disabled={isCantStartGame}>
          start
        </button>
      </RoomButtonsWrapper>
    </GeneralWrapper>
  );
};
