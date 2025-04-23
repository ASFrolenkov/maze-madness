import React, { FC } from "react";
import { PlayerWrapper, PlayerButton, PlayerGroup, KickButton } from "./styled";

export const Players: FC<{
  players: string[];
  currentUsername: string;
  kickPlayerCb: (playerName: string) => void;
}> = ({ players, currentUsername, kickPlayerCb }) => {
  const isHost = players[0] === currentUsername;
  return (
    <PlayerWrapper>
      {players.map((player, index) => {
        if (index === 0)
          return <PlayerButton key={player}>{player}</PlayerButton>;
        return (
          <PlayerGroup key={player}>
            <PlayerButton>{player}</PlayerButton>
            {isHost && (
              <KickButton
                onClick={() => {
                  kickPlayerCb(player);
                }}
              >
                kick
              </KickButton>
            )}
          </PlayerGroup>
        );
      })}
    </PlayerWrapper>
  );
};
