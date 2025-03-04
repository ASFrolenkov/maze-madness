import { FC } from "react";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";
import { MainMenuUI } from "UI";

interface ComponentProps {
  game: IRefPhaserGame | null;
}

export const FactoryUI: FC<ComponentProps> = ({ game }) => {
  return <>{game?.scene && <MainMenuUI game={game} />}</>;
};
