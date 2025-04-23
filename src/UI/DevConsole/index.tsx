import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import { GameUI } from "Types/game";
import { Block, ConsoleWrapper, Title } from "./styled";
import PhaserScene from "Core/PhaserScene";
import { SceneNames } from "Constants";
import { axiosMM } from "API";

const appElement = document.getElementById("app");

export const DevConsole: GameUI = ({ game }) => {
  const [gameFPS, setGameFps] = useState(0);
  const [delta, setDelta] = useState(0);

  const sendLogin = (username?: string, password?: string) =>
    axiosMM
      .post("/auth/login", {
        username,
        password,
      })
      .then(() => {
        game.scene?.registry.set("username", username);
        game.scene?.nextScene();
      });

  useEffect(() => {
    const updateListener = (_: number, delta: number) => {
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

  const getScenes = () => {
    const sceneManager = game.scene?.scene.manager;

    return sceneManager?.scenes as PhaserScene[];
  };

  const logout = () => {
    axiosMM.post("/auth/logout").then(() => {
      game.scene?.scene.start(SceneNames.Login);
    });
  };

  if (!appElement) return <></>;
  return createPortal(
    <ConsoleWrapper>
      <Title>Dev Console</Title>
      <div>FPS: {gameFPS}</div>
      <div>delta: {delta}</div>
      <div>current scene: {game.scene?.scene.key}</div>
      <Block>
        <button onClick={game.scene?.prevScene.bind(game.scene)}>
          prev scene
        </button>
        <button onClick={game.scene?.nextScene.bind(game.scene)}>
          next scene
        </button>
      </Block>

      <Block>
        {getScenes()?.map((scene) => (
          <button
            key={scene.scene.key}
            onClick={() => {
              game.scene?.changeScene(scene.scene.key as SceneNames);
            }}
          >
            {scene.scene.key}
          </button>
        ))}
      </Block>
      <Block>
        <button onClick={logout}>logout</button>
      </Block>
      <Block>
        <button onClick={() => sendLogin("sosal", "sosal2")}>sosal</button>
        <button onClick={() => sendLogin("test", "testtest")}>test</button>
      </Block>
    </ConsoleWrapper>,
    appElement
  );
};
