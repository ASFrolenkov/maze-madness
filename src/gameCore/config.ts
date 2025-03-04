import { AUTO, Game } from "phaser";
import scenes from "Scenes";

const config: Phaser.Types.Core.GameConfig = {
  type: AUTO,
  width: 1024,
  height: 768,
  backgroundColor: "#028af8",
  scene: scenes,
};

const StartGame = (parent: string) => {
  return new Game({ ...config, parent });
};

export default StartGame;
