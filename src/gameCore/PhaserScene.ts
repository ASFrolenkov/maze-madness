import { SceneNames } from "Constants";
import { Scene } from "phaser";
import EventBus from "./EventBus";
import { io, Socket } from "socket.io-client";
import { logger } from "Helpers";

type SceneWithCreate = Scene & { create: () => void };

export default class PhaserScene extends Scene {
  constructor(sceneName: SceneNames) {
    super(sceneName);

    const that = this as unknown as SceneWithCreate;
    // get "create" method from instance PhaserScene
    const originalCreate = that.create;
    that.create = (...args) => {
      // call create method from instance PhaserScene
      originalCreate?.apply(this, args);
      this.registerScene();
    };
  }

  changeScene(sceneName: SceneNames) {
    this.scene.start(sceneName);
  }

  nextScene() {
    const manager = this.scene.manager;
    if (manager.getIndex(this) + 1 <= manager.scenes.length)
      this.scene.start(manager.getAt(manager.getIndex(this) + 1));
  }

  prevScene() {
    const manager = this.scene.manager;
    if (manager.getIndex(this) - 1 >= 0)
      this.scene.start(manager.getAt(manager.getIndex(this) - 1));
  }

  onShutdown(callback: () => void) {
    logger.info("EVENTS", `${this.scene.key} start shutdown listening`);
    this.events.once("shutdown", () => {
      callback();
      logger.info("EVENTS", `${this.scene.key} remove shutdown listening`);
    });
  }

  private registerScene() {
    EventBus.emit("current-scene-ready", this);
    logger.info("SCENE", `${this.scene.key} is ready`);
  }

  startSocket(username: string, roomName: string) {
    if (this.game.registry.get("socket")) {
      this.game.registry.remove("socket");
    }
    const socket = io(import.meta.env.VITE_SOCKET_ADDRESS, {
      query: {
        roomName,
        username,
      },
    });
    this.game.registry.set("socket", socket);
    this.game.registry.set("sessionId", socket.id);
  }

  getSocket(): Socket | undefined {
    return this.game.registry.get("socket");
  }

  getSessionId(): string | undefined {
    logger.info("SCENE", "get sid", this.game.registry.get("socket").id);
    return this.game.registry.get("socket").id;
  }

  emitSocket(event: string, ...args: unknown[]): void {
    const socket = this.game.registry.get("socket") as Socket;
    if (socket) socket.emit(event, ...args);
  }

  addSocketListener<Response = unknown>(
    event: string,
    listener: (...arg: Response[]) => void
  ) {
    const socket = this.game.registry.get("socket") as Socket;
    if (socket) socket.on(event, listener);
  }

  removeSocketListener<Response = unknown>(
    event: string,
    listener: (...arg: Response[]) => void
  ) {
    const socket = this.game.registry.get("socket") as Socket;
    if (socket) socket.off(event, listener);
  }

  clearSocketListener<Response = unknown>(
    event: string,
    listener: (...arg: Response[]) => void
  ) {
    const socket = this.game.registry.get("socket") as Socket;
    if (socket) socket.off(event, listener);
  }

  disconnectSocket() {
    const socket = this.game.registry.get("socket") as Socket;
    if (socket) {
      socket.disconnect();
      socket.removeAllListeners();
    }
  }
}
