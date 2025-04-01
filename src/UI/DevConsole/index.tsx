import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import { GameUI } from "Types/game";
import { ConsoleWrapper, Title } from "./styled";

const appElement = document.getElementById("app");

export const DevConsole: GameUI = ({ game }) => {
  const [gameFPS, setGameFps] = useState(0);
  const [delta, setDelta] = useState(0);

  useEffect(() => {
    const updateListener = (time: number, delta: number) => {
      setDelta(Math.round(delta) / 1000);
      if (game.game?.loop.actualFps)
        setGameFps(Math.round(game.game.loop.actualFps) || 0);
    };
    if (game) {
      game.scene?.events.on("update", updateListener);
    }
    return () => {
      game.scene?.events.off("update", updateListener);
    };
  }, [game]);

  const changeScene = () => {
    game.scene?.changeScene();
  };

  if (!appElement) return <></>;
  return createPortal(
    <ConsoleWrapper>
      <Title>Dev Console</Title>
      <div>FPS: {gameFPS}</div>
      <div>delta: {delta}</div>
      <button onClick={changeScene}>change scene</button>
    </ConsoleWrapper>,
    appElement
  );
};
