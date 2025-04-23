import bg from "Assets/w1_bw_minotaur_org.png";
import PhaserScene from "Core/PhaserScene";
import { SceneNames } from "Constants";

export default class Boot extends PhaserScene {
  constructor() {
    super(SceneNames.Boot);
  }

  preload() {
    const { load } = this;
    // загружется 2 раза, почему?
    load.image("background", bg);
  }

  create() {
    this.nextScene();
  }
}
