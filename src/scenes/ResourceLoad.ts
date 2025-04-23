import ground from "Assets/maps/grass.png";
import props from "Assets/maps/props.png";
import pIdle from "Assets/characters/playerIdle.png";
import pRun from "Assets/characters/playerRun.png";
import JSONMap from "Assets/maps/authorization_placeholder.json";
import PhaserScene from "Core/PhaserScene";
import { SceneNames } from "Constants";

const loaderWidth = 468;

export default class ResourceLoad extends PhaserScene {
  sceneTimer: NodeJS.Timeout;

  constructor() {
    super(SceneNames.ResourceLoad);
  }

  private getLoader() {
    this.add
      .image(0, 0, "background")
      .setOrigin(0, 0)
      .setDisplaySize(this.scale.width, this.scale.height);

    //  A simple progress bar. This is the outline of the bar.
    const loaderStroke = this.add
      .rectangle(this.scale.width / 2, this.scale.height / 2, loaderWidth, 32)
      .setStrokeStyle(1, 0xffffff);

    //  This is the progress bar itself. It will increase in size from the left based on the % of progress.
    const loaderBar = this.add
      .rectangle(
        this.scale.width / 2 - loaderWidth / 2 + 1,
        this.scale.height / 2,
        4,
        29,
        0xffffff
      )
      .setOrigin(0, 0.5);
    loaderBar.width = loaderWidth * this.load.progress;

    // Зарефакторить
    if (this.load.progress === 1) {
      loaderStroke.destroy();
      loaderBar.destroy();

      this.add
        .text(this.scale.width / 2, this.scale.height / 2, "Loading Complete", {
          fontFamily: "Arial Black",
          fontSize: 24,
          color: "#413c4d",
          stroke: "#ff9d9d",
          strokeThickness: 2,
          align: "center",
        })
        .setOrigin(0.5);
    }

    //  Use the 'progress' event emitted by the LoaderPlugin to update the loading bar
    this.load.on(
      "progress",
      (progress: number) => {
        //  Update the progress bar (our bar is 464px wide, so 100% = 464px)
        setTimeout(() => {
          loaderBar.width = (loaderWidth - 3) * progress;
          // Зарефакторить
          if (this.load.progress === 1) {
            setTimeout(() => {
              loaderStroke.destroy();
              loaderBar.destroy();

              this.add
                .text(
                  this.scale.width / 2,
                  this.scale.height / 2,
                  "Loading Complete",
                  {
                    fontFamily: "Arial Black",
                    fontSize: 24,
                    color: "#413c4d",
                    stroke: "#ff9d9d",
                    strokeThickness: 2,
                    align: "center",
                  }
                )
                .setOrigin(0.5);
            }, 1000);
          }
        }, 100);
      },
      this
    );
  }

  init() {
    this.getLoader();

    this.onShutdown(() => {
      clearTimeout(this.sceneTimer);
    });
  }

  preload() {
    const { load } = this;

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
    const playerIdleAnimation = this.scene.scene.anims.create({
      key: "playerIdle",
      frames: this.scene.scene.anims.generateFrameNumbers("playerIdle", {
        start: 0,
        end: 9,
      }),
      frameRate: 6,
      repeat: -1,
    });

    const playerRunAnimation = this.scene.scene.anims.create({
      key: "playerRun",
      frames: this.scene.scene.anims.generateFrameNumbers("playerRun", {
        start: 0,
        end: 9,
      }),
      frameRate: 12,
      repeat: -1,
    });

    if (playerRunAnimation && playerIdleAnimation && this.load.progress === 1) {
      this.sceneTimer = setTimeout(() => {
        // this.nextScene();
        this.changeScene(SceneNames.Login);
      }, 2000);
    }
  }
}
