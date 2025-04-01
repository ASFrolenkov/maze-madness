import { AUTO, Game } from "phaser";
import scenes from "Scenes";

const config: Phaser.Types.Core.GameConfig = {
  type: AUTO,
  physics: {
    default: "arcade",
    arcade: {
      debug: true,
      gravity: { x: 0, y: 0 },
    },
  },
  scale: {
    mode: Phaser.Scale.FIT,
    autoCenter: Phaser.Scale.CENTER_BOTH,
    width: 640, // размеры карты, если передать другие значения то будут полосы
    height: 360, // размеры карты, если передать другие значения то будут полосы
  },
  backgroundColor: "#95d4f3",
  scene: scenes,
  pixelArt: true,
  fps: {
    limit: 60,
  },
};

const StartGame = (parent: string) => {
  return new Game({ ...config, parent });
};

export default StartGame;
