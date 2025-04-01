import { Scene } from "phaser";
import { io } from "socket.io-client";
import bg from "Assets/w1_bw_minotaur_org.png";

export const socket = io(import.meta.env.VITE_SOCKET_ADDRESS);

export default class Boot extends Scene {
  constructor() {
    super("Boot");
  }

  preload() {
    const { load } = this;

    load.image("background", bg);
  }

  create() {
    // Возможны ошибки если сокет не успел подключится. Доабвить проверки
    // Проблема передачи sessionId в хранилище. Мб из-за того что сцены разные
    this.registry.set("sessionId", socket.id);

    window.addEventListener("unload", () => {
      socket.disconnect();
    });

    socket.emit("ping", "hello world");

    socket.onAny((event: string, ...args: unknown[]) => {
      console.log(`onSocket-${event}`);
      this.game.events.emit(`onSocket-${event}`, args);
    });

    this.game.events.on("emitSocket", (event: string, ...args: unknown[]) => {
      socket.emit(event, ...args);
    });

    this.scene.start("Preloader");
  }
}
