import { Scene } from "phaser";
import star from "Assets/star.png";
import ground from "Assets/maps/grass.png";
import props from "Assets/maps/props.png";
import pIdle from "Assets/characters/playerIdle.png";
import pRun from "Assets/characters/playerRun.png";
import JSONMap from "Assets/maps/authorization_placeholder.json";

export default class Preloader extends Scene {
  constructor() {
    super("Preloader");
  }

  init() {
    //  We loaded this image in our Boot Scene, so we can display it here
    this.add.image(0, 0, "background").setOrigin(0, 0);

    //  A simple progress bar. This is the outline of the bar.
    this.add.rectangle(512, 384, 468, 32).setStrokeStyle(1, 0xffffff);

    //  This is the progress bar itself. It will increase in size from the left based on the % of progress.
    const bar = this.add.rectangle(512 - 230, 384, 4, 28, 0xffffff);

    //  Use the 'progress' event emitted by the LoaderPlugin to update the loading bar
    this.load.on("progress", (progress: number) => {
      //  Update the progress bar (our bar is 464px wide, so 100% = 464px)
      setTimeout(() => {
        bar.width = 4 + 460 * progress;
      }, 100);
    });
  }

  preload() {
    const { load } = this;

    load.image("star", star);
    load.image("ground", ground);
    load.image("props", props);
    load.spritesheet("playerIdle", pIdle, {
      frameWidth: 120,
      frameHeight: 80,
    });
    load.spritesheet("playerRun", pRun, {
      frameWidth: 120,
      frameHeight: 80,
    });
    load.tilemapTiledJSON("gameMap", JSONMap);
  }

  create() {
    this.scene.scene.anims.create({
      key: "playerIdle",
      frames: this.scene.scene.anims.generateFrameNumbers("playerIdle", {
        start: 0,
        end: 9,
      }),
      frameRate: 6,
      repeat: -1,
    });

    this.scene.scene.anims.create({
      key: "playerRun",
      frames: this.scene.scene.anims.generateFrameNumbers("playerRun", {
        start: 0,
        end: 9,
      }),
      frameRate: 12,
      repeat: -1,
    });
    //  When all the assets have loaded, it's often worth creating global objects here that the rest of the game can use.
    //  For example, you can define global animations here, so we can use them in other scenes.

    //  Move to the MainMenu. You could also swap this for a Scene Transition, such as a camera fade.
    this.scene.start("MainMenu");
  }
}
