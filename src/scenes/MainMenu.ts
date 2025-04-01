import { GameObjects, Scene } from "phaser";

import { EventBus } from "../gameCore/EventBus";
import { ServerResponse } from "Types/player";

export default class MainMenu extends Scene {
  background: GameObjects.Image;
  title: GameObjects.Text;
  logoTween: Phaser.Tweens.Tween | null;

  constructor() {
    super("MainMenu");
  }

  create() {
    this.background = this.add
      .image(0, 0, "background")
      .setOrigin(0, 0)
      .setDisplaySize(this.scale.width, this.scale.height);

    this.title = this.add
      .text(this.scale.width / 2, this.scale.height / 2 - 300, "Maze Madness", {
        fontFamily: "Arial Black",
        fontSize: 38,
        color: "#413c4d",
        stroke: "#ff9d9d",
        strokeThickness: 8,
        align: "center",
      })
      .setOrigin(0.5, 0.5)
      .setDepth(100);
    this.setEvents();

    EventBus.emit("current-scene-ready", this);
  }

  private setEvents() {
    this.game.events.on("onSocket-playerCreated", (args: string) => {
      const parsedResponse: ServerResponse = JSON.parse(args);
      this.game.registry.set("players", parsedResponse);
      this.scene.start("Game");
    });
  }

  changeScene() {
    if (this.logoTween) {
      this.logoTween.stop();
      this.logoTween = null;
    }

    this.scene.start("Game");
  }

  moveLogo(vueCallback: ({ x, y }: { x: number; y: number }) => void) {
    if (this.logoTween) {
      if (this.logoTween.isPlaying()) {
        this.logoTween.pause();
      } else {
        this.logoTween.play();
      }
    } else {
      this.logoTween = this.tweens.add({
        targets: this.title,
        x: { value: 750, duration: 3000, ease: "Back.easeInOut" },
        y: { value: 80, duration: 1500, ease: "Sine.easeOut" },
        yoyo: true,
        repeat: -1,
        onUpdate: () => {
          if (vueCallback) {
            vueCallback({
              x: Math.floor(this.title.x),
              y: Math.floor(this.title.y),
            });
          }
        },
      });
    }
  }
}
