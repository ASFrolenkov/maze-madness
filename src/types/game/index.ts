import { FC } from "react";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";

export type GameUI<AdditionalProps extends object | void = void> =
  AdditionalProps extends object
    ? FC<
        {
          game: IRefPhaserGame;
        } & AdditionalProps
      >
    : FC<{
        game: IRefPhaserGame;
      }>;
