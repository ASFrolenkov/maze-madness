import { useState } from "react";
import { FactoryUI } from "UI";
import { IRefPhaserGame, PhaserGame } from "Core";

function App() {
  const [game, setGame] = useState<IRefPhaserGame | null>(null);

  return (
    <>
      <PhaserGame ref={setGame} />
      <FactoryUI game={game} />
    </>
  );
}

export default App;
