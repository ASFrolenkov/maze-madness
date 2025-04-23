import PhaserScene from "Core/PhaserScene";
import { SceneNames } from "Constants";

export default class Login extends PhaserScene {
  logoTween: Phaser.Tweens.Tween | null;

  constructor() {
    super(SceneNames.Login);
  }

  preload() {}

  create() {
    this.add
      .image(0, 0, "background")
      .setOrigin(0, 0)
      .setDisplaySize(this.scale.width, this.scale.height);

    this.add
      .text(this.scale.width / 2, this.scale.height / 2 - 75, "Maze Madness", {
        fontFamily: "Arial Black",
        fontSize: 38,
        color: "#413c4d",
        stroke: "#ff9d9d",
        strokeThickness: 4,
        align: "center",
      })
      .setOrigin(0.5);
  }
}
