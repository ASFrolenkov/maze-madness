import { EventBus } from "../gameCore/EventBus";
import { Scene } from "phaser";
import { Player } from "Entities";
import JSONMap from "Assets/maps/authorization_placeholder.json";
import { PlayerResponse, ServerResponse } from "Types/player";

export default class Game extends Scene {
  camera: Phaser.Cameras.Scene2D.Camera;
  background: Phaser.GameObjects.Image;
  gameText: Phaser.GameObjects.Text;
  playersState: Map<string | number, Player> = new Map();
  currentPlayerId: number | string;

  constructor() {
    super("Game");
  }

  create() {
    this.addPlayers();
    this.createMap();
    this.createCamera();
    this.setEvents();

    EventBus.emit("current-scene-ready", this);
  }

  private addPlayers() {
    const players: ServerResponse = this.game.registry.get("players");
    const currentSessionKey = this.registry.get("sessionId");

    Object.keys(players).forEach((sessionKey) => {
      const { id, x, y, name } = players[sessionKey];
      console.log(name);
      if (sessionKey === currentSessionKey) {
        this.currentPlayerId = id;
        this.playersState.set(
          id,
          new Player(this, x, y, "playerIdle", 0)
            .setId(id)
            .setName(name)
            .setPlayable()
        );
      } else {
        this.playersState.set(
          id,
          new Player(this, x, y, "playerIdle", 0).setId(id).setName(name)
        );
      }
    });
  }

  private setEvents() {
    this.game.events.on("onSocket-playerDisconnected", (args: string) => {
      const playerResoponse: PlayerResponse = JSON.parse(args);
      const { id } = playerResoponse;

      const player = this.playersState.get(id);
      player?.destroyPlayer();
      this.playersState.delete(id);
    });
    this.game.events.on("onSocket-playerConnected", (args: string) => {
      const parsedResponse: PlayerResponse = JSON.parse(args);
      const { id, x, y, name } = parsedResponse;
      this.playersState.set(
        id,
        new Player(this, x, y, "playerIdle", 0).setId(id).setName(name)
      );
    });
  }

  private createMap() {
    const map = this.make.tilemap({ key: "gameMap" });
    const ground = map.addTilesetImage(JSONMap.tilesets[0].name, "ground");
    const props = map.addTilesetImage(JSONMap.tilesets[1].name, "props");

    let propsLayer;
    if (ground && props) {
      map.createLayer("ground", ground);
      propsLayer = map.createLayer("props", props);
      map.createLayer("effects", props);
    }

    this.cameras.main.setBounds(0, 0, map.widthInPixels, map.heightInPixels);
    this.physics.world.setBounds(0, 0, map.widthInPixels, map.heightInPixels);

    const currentPlayer = this.playersState.get(this.currentPlayerId);

    if (currentPlayer && propsLayer && props) {
      propsLayer.forEachTile((tile) => {
        {
          const tileWorldPos = propsLayer.tileToWorldXY(tile.x, tile.y);
          const collisionGroup = props.getTileCollisionGroup(tile.index);

          if (!collisionGroup || collisionGroup.objects.length === 0) {
            return;
          }

          // The group will have an array of objects - these are the individual collision shapes
          const objects = collisionGroup.objects;

          for (let i = 0; i < objects.length; i++) {
            const object = objects[i];
            const objectX = tileWorldPos.x + object.x;
            const objectY = tileWorldPos.y + object.y;

            const physicBody = this.physics.add.staticBody(
              objectX,
              objectY,
              object.width,
              object.height
            );
            this.physics.add.collider(currentPlayer, physicBody);
          }
        }
      });
    }

    return map;
  }

  private createCamera() {
    this.cameras.main.backgroundColor.setFromRGB({
      r: 255,
      g: 0,
      b: 0,
      a: 0.3,
    });
    this.cameras.main.setDeadzone(100, 100);
  }

  private showDebugWalls(propsLayer: Phaser.Tilemaps.TilemapLayer) {
    const debugGraphics = this.add.graphics();
    propsLayer.renderDebug(debugGraphics, {
      tileColor: null,
      collidingTileColor: new Phaser.Display.Color(255, 0, 0, 100),
    });
  }

  changeScene() {
    this.playersState.forEach((player) => {
      console.log("destroy player");
      player.destroyPlayer();
    });

    this.scene.start("GameOver");
  }
}
