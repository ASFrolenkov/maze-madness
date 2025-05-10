import { SceneNames } from "Constants";
import PhaserScene from "Core/PhaserScene";

export default class Lobbies extends PhaserScene {
  constructor() {
    super(SceneNames.Lobbies);
  }

  create() {
    this.add
      .image(0, 0, "background")
      .setOrigin(0)
      .setDisplaySize(this.scale.width, this.scale.height);

    this.add
      .text(this.scale.width / 2, this.scale.height / 2 - 75, "Lobbies", {
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
