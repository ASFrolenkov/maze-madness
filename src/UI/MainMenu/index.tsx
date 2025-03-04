import { FC, useEffect, useState } from "react";
import MainMenu from "Scenes/MainMenu";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";

interface ComponentProps {
  game: IRefPhaserGame | null;
}

export const MainMenuUI: FC<ComponentProps> = ({ game }) => {
  const [spritePosition, setSpritePosition] = useState({ x: 0, y: 0 });
  const [canMoveSprite, setCanMoveSprite] = useState(true);

  const currentScene = game?.scene;

  useEffect(() => {
    setCanMoveSprite(false);
    if (currentScene?.scene.key !== "MainMenu") {
      setCanMoveSprite(true);
    }
  }, [currentScene?.scene.key]);

  const changeScene = () => {
    if (currentScene) {
      const scene = currentScene as MainMenu;
      scene.changeScene();
    }
  };

  const moveSprite = () => {
    if (currentScene) {
      const scene = currentScene as MainMenu;

      if (scene.scene.key === "MainMenu") {
        scene.moveLogo(({ x, y }) => {
          setSpritePosition({ x, y });
        });
      }
    }
  };

  const addSprite = () => {
    if (currentScene) {
      const x = Phaser.Math.Between(64, currentScene.scale.width - 64);
      const y = Phaser.Math.Between(64, currentScene.scale.height - 64);

      const star = currentScene.add.sprite(x, y, "star");

      currentScene.add.tween({
        targets: star,
        duration: 500 + Math.random() * 1000,
        alpha: 0,
        yoyo: true,
        repeat: -1,
      });
    }
  };
  return (
    <div>
      <h1>Main Menu UI</h1>
      <div>
        <button className="button" onClick={changeScene}>
          Change Scene
        </button>
      </div>
      <div>
        <button
          disabled={canMoveSprite}
          className="button"
          onClick={moveSprite}
        >
          Toggle Movement
        </button>
      </div>
      <div className="spritePosition">
        Sprite Position:
        <pre>{`{\n  x: ${spritePosition.x}\n  y: ${spritePosition.y}\n}`}</pre>
      </div>
      <div>
        <button className="button" onClick={addSprite}>
          Add New Sprite
        </button>
      </div>
    </div>
  );
};
