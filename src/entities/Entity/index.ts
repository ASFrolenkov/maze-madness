import { Scene } from "phaser";

export abstract class Entity extends Phaser.Physics.Arcade.Sprite {
  baseHealth = 100;
  baseSpeed = 100;

  constructor(
    scene: Scene,
    x: number,
    y: number,
    texture: string | Phaser.Textures.Texture,
    frame?: string | number
  ) {
    super(scene, x, y, texture, frame);

    scene.physics.add.existing(this);
    scene.add.existing(this);
  }
}
