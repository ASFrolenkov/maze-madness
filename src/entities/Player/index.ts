import { Scene } from "phaser";
import { Entity } from "../Entity";
import { ServerResponse } from "Types/player";

export class Player extends Entity {
  stamina = 100;
  private playerName: Phaser.GameObjects.Text;
  localScene: Scene;
  private directory = {
    up: false,
    down: false,
    left: false,
    right: false,
  };
  private isPlayable: boolean;
  private id: number | string;

  private prevPosX: number;
  private prevPosY: number;
  private serverPosX: number;
  private serverPosY: number;

  constructor(
    scene: Scene,
    x: number,
    y: number,
    texture: string | Phaser.Textures.Texture,
    frame?: string | number
  ) {
    super(scene, x, y, texture, frame);

    this.prevPosX = this.x;
    this.prevPosY = this.y;
    this.serverPosX = this.x;
    this.serverPosY = this.y;

    this.localScene = scene;

    this.setDepth(1);
    this.setBodySize(22, 16); // костыль для подгонки коллайдера под персонажа
    this.setOffset(45, 65); // костыль для подгонки коллайдера под персонажа

    // идет спам на бэк
    scene.events.on("update", this.onUpdate, this);

    this.serverSync();
    if (!this.isPlayable) {
      //this.setCollisionCategory(2);
      scene.game.events.on("onSocket-playerMoved", (args: string) => {
        // можно переделать на получение игрока по ключу sessionKey, для этого надо сохранять sessionKey внутри игрока
        const serverResponse: ServerResponse = JSON.parse(args);
        Object.values(serverResponse).some((player) => {
          if (player.id === this.id) {
            this.serverPosX = player.x;
            this.serverPosY = player.y;
            return true;
          }
        });
      });
    }
  }
  // вынести отдельную функцию для передвижения? прогонять через эту функцию локальные координаты и координаты с сервера (интерполировать)
  private playerMove() {
    let velocityX = 0;
    let velocityY = 0;

    if (this.directory.up) {
      velocityY += -1;
    }
    if (this.directory.down) {
      velocityY += 1;
    }
    if (this.directory.right) {
      velocityX += 1;
    }
    if (this.directory.left) {
      velocityX += -1;
    }

    if (velocityX !== 0 || velocityY !== 0) {
      const length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
      velocityX /= length;
      velocityY /= length;
    }

    velocityX *= this.baseSpeed;
    velocityY *= this.baseSpeed;

    if (velocityX || velocityY) {
      this.play("playerRun", true);
    } else {
      this.play("playerIdle", true);
    }

    if (velocityX < 0) {
      this.setFlipX(true);
      this.setOffset(54, 65); // костыль для подгонки коллайдера под персонажа
    } else if (velocityX > 0) {
      this.setFlipX(false);
      this.setOffset(45, 65); // костыль для подгонки коллайдера под персонажа
    }

    this.setVelocity(velocityX, velocityY);
    this.sendPlayerPos();
  }

  private sendPlayerPos() {
    if (this.prevPosX !== this.x || this.prevPosY !== this.y) {
      this.localScene.game.events.emit(
        "emitSocket",
        "playerMove",
        JSON.stringify({ x: this.x, y: this.y })
      );

      this.prevPosX = this.x;
      this.prevPosY = this.y;
      this.updateName();
    }
  }

  private serverSync() {
    this.localScene.game.events.on("onSocket-playerMoved", (args: string) => {
      const serverResponse = JSON.parse(args) as {
        x: number;
        y: number;
        anim: string;
      };

      this.serverPosX = serverResponse.x;
      this.serverPosY = serverResponse.y;
    });

    this.localScene.game.events.on("onSocket-playerCreated", (args: string) => {
      const serverResponse = JSON.parse(args) as {
        x: number;
        y: number;
        anim: string;
      };

      this.serverPosX = serverResponse.x;
      this.serverPosY = serverResponse.y;
    });
  }

  private serverMove() {
    if (
      this.prevPosX !== this.serverPosX ||
      this.prevPosY !== this.serverPosY
    ) {
      this.play("playerRun", true);
      this.x = Phaser.Math.Linear(this.prevPosX, this.serverPosX, 0.9);
      this.y = Phaser.Math.Linear(this.prevPosY, this.serverPosY, 0.9);
      if (this.prevPosX > this.x) {
        this.setFlipX(true);
        this.setOffset(54, 65);
      } else if (this.prevPosX < this.x) {
        this.setFlipX(false);
        this.setOffset(45, 65);
      }
      this.prevPosX = this.x;
      this.prevPosY = this.y;
      this.updateName();
    } else {
      this.play("playerIdle", true);
    }
  }

  private updateName() {
    // добавить интерполяцию
    this.playerName.setX(this.x);
    this.playerName.setY(this.y + 50);
  }

  private onUpdate() {
    if (this && this.isPlayable) {
      this.playerMove();
    } else {
      this.serverMove();
    }
  }

  setId(id: string | number) {
    this.id = id;
    console.log("create player", this.id);
    return this;
  }

  setPlayable() {
    this.scene.input.keyboard?.on("keydown", (arg: KeyboardEvent) => {
      const key = arg.code;
      if (key === "KeyW") {
        this.directory.up = true;
      }
      if (key === "KeyD") {
        this.directory.right = true;
      }
      if (key === "KeyS") {
        this.directory.down = true;
      }
      if (key === "KeyA") {
        this.directory.left = true;
      }
    });

    this.scene.input.keyboard?.on("keyup", (arg: KeyboardEvent) => {
      const key = arg.code;
      if (key === "KeyW") {
        this.directory.up = false;
      }
      if (key === "KeyD") {
        this.directory.right = false;
      }
      if (key === "KeyS") {
        this.directory.down = false;
      }
      if (key === "KeyA") {
        this.directory.left = false;
      }
    });

    this.isPlayable = true;
    this.setCollideWorldBounds(true);
    this.localScene.cameras.main.startFollow(this);

    return this;
  }

  setName(name: string) {
    this.playerName = this.localScene.add
      .text(this.x, this.y + 50, name, {
        fontFamily: "Arial Black",
        fontSize: 16,
        color: "#fff",
        align: "center",
      })
      .setOrigin(0.5, 0.5)
      .setDepth(100);
    return this;
  }

  destroyPlayer() {
    // добавить удаление ника
    this.localScene.events.off("update", this.onUpdate, this);
    if (this.isPlayable) {
      this.localScene.game.events.emit("emitSocket", "playerDisconnect");
    }
    this.destroy();
    this.playerName.destroy();
  }
}
