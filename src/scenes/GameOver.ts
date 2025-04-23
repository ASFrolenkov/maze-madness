import { SceneNames } from "Constants";
import PhaserScene from "Core/PhaserScene";

export default class GameOver extends PhaserScene {
  camera: Phaser.Cameras.Scene2D.Camera;
  background: Phaser.GameObjects.Image;
  gameOverText: Phaser.GameObjects.Text;

  constructor() {
    super(SceneNames.GameOver);
  }

  create() {
    this.camera = this.cameras.main;
    this.camera.setBackgroundColor(0xff0000);

    this.background = this.add
      .image(0, 0, "background")
      .setOrigin(0, 0)
      .setDisplaySize(this.scale.width, this.scale.height);
    this.background.setAlpha(0.5);

    this.gameOverText = this.add
      .text(this.scale.width / 2, this.scale.height / 2, "Game Over", {
        fontFamily: "Arial Black",
        fontSize: 64,
        color: "#ffffff",
        stroke: "#000000",
        strokeThickness: 8,
        align: "center",
      })
      .setOrigin(0.5);
  }
}
