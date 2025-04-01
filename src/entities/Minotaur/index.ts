import { Entity } from "../Entity";

export class Minotaur extends Entity {
  attack = 55;
  speed = this.baseSpeed + 10;

  constructor(
    scene: Phaser.Scene,
    x: number,
    y: number,
    texture: string | Phaser.Textures.Texture,
    frame?: string | number
  ) {
    super(scene, x, y, texture, frame);
  }
}
