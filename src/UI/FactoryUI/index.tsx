import { SceneNames } from "Constants";
import { FC } from "react";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";
import { DevConsole, LobbyUI, LoginUI, RoomUI } from "UI";

interface ComponentProps {
  game: IRefPhaserGame | null;
}

export const FactoryUI: FC<ComponentProps> = ({ game }) => {
  if (!game) return <></>;

  const getInterface = () => {
    switch (game.scene?.scene.key) {
      case SceneNames.Lobbies:
        return <LobbyUI game={game} />;
      case SceneNames.Login:
        return <LoginUI game={game} />;
      case SceneNames.Room:
        return <RoomUI game={game} />;
      default:
        return <></>;
    }
  };

  return (
    <>
      <>{getInterface()}</>
      <DevConsole game={game} />
    </>
  );
};
