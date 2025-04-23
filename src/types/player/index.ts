export interface PlayerResponse {
  id: number | string;
  playerColor: string;
  x: number;
  y: number;
  name: string;
}

export type ServerResponse = {
  [SessionKey: string]: PlayerResponse;
};

export type PlayerServerResponse = {
  [username: string]: {
    playerSessionId: string;
    name: string;
    currX: number;
    currY: number;
  };
};
