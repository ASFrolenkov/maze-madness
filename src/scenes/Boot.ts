import { Scene } from "phaser";
import { io } from "socket.io-client";

const socket = io(import.meta.env.VITE_SOCKET_ADDRESS);

export default class Boot extends Scene {
  constructor() {
    super("Boot");
  }

  preload() {
    this.load.image("background", "assets/bg.png");
  }

  create() {
    socket.emit("ping", "hello world");

    socket.onAny((...args) => {
      this.events.emit("onSocket", args);
    });

    this.events.on("emitSocket", (event: string, ...args: unknown[]) => {
      socket.emit(event, ...args);
    });

    this.scene.start("Preloader");
  }
}
