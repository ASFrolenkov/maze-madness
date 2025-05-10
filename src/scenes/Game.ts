import { Player } from "Entities";
import JSONMap from "Assets/maps/authorization_placeholder.json";
import { PlayerResponse, PlayerServerResponse } from "Types/player";
import PhaserScene from "Core/PhaserScene";
import { SceneNames } from "Constants";
import { logger } from "Helpers";

export default class Game extends PhaserScene {
  camera: Phaser.Cameras.Scene2D.Camera;
  background: Phaser.GameObjects.Image;
  gameText: Phaser.GameObjects.Text;
  playersState: Map<string | number, Player> = new Map();
  currentPlayerId: number | string;

  constructor() {
    super(SceneNames.Game);
  }

  preload() {
    this.onShutdown(() => {
      if (this.playersState.entries.length) {
        this.playersState.forEach((player) => {
          logger.info("SCENE", "destroy player");
          player.destroyPlayer();
        });
      }
    });
  }

  create() {
    this.addPlayers();
    this.createMap();
    this.createCamera();
    this.setEvents();
  }

  private addPlayers() {
    const players: PlayerServerResponse = this.game.registry.get("players");
    const currentSessionID = this.getSessionId();

    Object.keys(players).forEach((username) => {
      const { playerSessionId, currX, currY, name } = players[username];
      logger.info("SOCKET", playerSessionId, currentSessionID);
      if (playerSessionId === currentSessionID) {
        this.currentPlayerId = playerSessionId;
        this.playersState.set(
          playerSessionId,
          new Player(this, currX, currY, "playerIdle", 0)
            .setId(playerSessionId)
            .setName(name)
            .setPlayable()
        );
      } else {
        this.playersState.set(
          playerSessionId,
          new Player(this, currX, currY, "playerIdle", 0)
            .setId(playerSessionId)
            .setName(name)
        );
      }
    });
  }

  private setEvents() {
    this.addSocketListener("playerDisconnectedServer", (args: string) => {
      const playerResoponse: PlayerResponse = JSON.parse(args);
      const { id } = playerResoponse;

      const player = this.playersState.get(id);
      player?.destroyPlayer();
      this.playersState.delete(id);
    });
    this.addSocketListener("playerConnectedServer", (args: string) => {
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
}
