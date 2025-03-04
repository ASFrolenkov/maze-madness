import { useState } from "react";
import { IRefPhaserGame, PhaserGame } from "./gameCore/PhaserGame";
import { FactoryUI } from "UI";

function App() {
  const [game, setGame] = useState<IRefPhaserGame | null>(null);

  return (
    <div id="app">
      <PhaserGame ref={setGame} />
      <FactoryUI game={game} />
    </div>
  );
}

export default App;
