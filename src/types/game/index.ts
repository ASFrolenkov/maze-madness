import { FC } from "react";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";

export type GameUI = FC<{ game: IRefPhaserGame }>;
