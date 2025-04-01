import { FC } from "react";
import { IRefPhaserGame } from "src/gameCore/PhaserGame";
import { DevConsole, MainMenuUI } from "UI";

interface ComponentProps {
  game: IRefPhaserGame | null;
}

export const FactoryUI: FC<ComponentProps> = ({ game }) => {
  if (!game) return <></>;

  const getInterface = () => {
    switch (game.scene?.scene.key) {
      case "MainMenu":
        return <MainMenuUI game={game} />;
      default:
        return <></>;
    }
  };

  return (
    <>
      <>{getInterface()}</>
      <DevConsole game={game} />
    </>
  );
};
