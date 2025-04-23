import { SceneNames } from "Constants";
import PhaserScene from "Core/PhaserScene";
import { logger } from "Helpers";

export default class Room extends PhaserScene {
  private roomName: string;
  constructor() {
    super(SceneNames.Room);
  }

  init() {
    this.roomName = this.registry.get("roomName");
    const username: string = this.registry.get("username");
    if (this.roomName && username) this.startSocket(username, this.roomName);
  }

  create() {
    // стакается выполнение
    this.onShutdown(() => {
      logger.info("SCENE", "shutdown room");
      // this.disconnectSocket();
    });

    this.add
      .image(0, 0, "background")
      .setOrigin(0)
      .setDisplaySize(this.scale.width, this.scale.height);

    this.add
      .text(
        this.scale.width / 2,
        this.scale.height / 2 - 75,
        this.roomName || "Room not found",
        {
          fontFamily: "Arial Black",
          fontSize: 38,
          color: "#413c4d",
          stroke: "#ff9d9d",
          strokeThickness: 4,
          align: "center",
        }
      )
      .setOrigin(0.5);
  }
}
