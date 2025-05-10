import { useEffect, useState } from "react";
import { GameUI } from "Types/game";
import { useModal } from "UI/hooks";
import {
  Loader,
  Lobbies,
  LobbiesBtns,
  LobbiesWrapper,
  LobbyContainer,
  LobbyDescription,
  LobbyName,
  Modal,
  ModalForm,
  ModalTitle,
  GeneralWrapper,
} from "./styled";
import { axiosMM } from "API";
import { SceneNames } from "Constants";

type Lobby = {
  name: string;
  players: number;
  maxPlayers: number;
  description: string;
  createdBy: string;
  isFull: boolean;
};

export const LobbyUI: GameUI = ({ game }) => {
  const [lobbies, setLobbies] = useState<Lobby[]>([]);
  const [fetched, setFetched] = useState(false);

  const { openModal, renderModal, closeModal } = useModal();

  const getRooms = () => {
    setFetched(false);
    return axiosMM
      .get<Lobby[]>("/socket/rooms")
      .then((response) => {
        setLobbies(response.data);
      })
      .catch((error) => console.log(error))
      .finally(() => setFetched(true));
  };

  useEffect(() => {
    getRooms();
  }, []);

  const createRoom = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const data = new FormData(event.target as HTMLFormElement);

    const name = data.get("name");
    const description = data.get("description");
    const maxPlayers = data.get("maxPlayers");
    const createdBy = game.scene?.registry.get("username");

    if (name && description && maxPlayers && createdBy)
      axiosMM
        .post<Lobby>("/socket/rooms", {
          name,
          description,
          maxPlayers,
          createdBy,
        })
        .then((response) => {
          game.scene?.registry.set("roomName", response.data.name);
        })
        .then(closeModal)
        .then(() => {
          game.scene?.scene.start(SceneNames.Room);
        });
  };

  // для теста, перевести на запрос
  const connectRoom = (lobbyName: string) => {
    game.scene?.registry.set("roomName", lobbyName);
    game.scene?.scene.start(SceneNames.Room);
  };

  const lobbiesNumber = lobbies?.length;
  return (
    <GeneralWrapper>
      {fetched && (
        <Lobbies>
          <LobbiesWrapper>
            {!!lobbiesNumber &&
              lobbies.map((lobby) => (
                <LobbyContainer
                  key={lobby.name}
                  $isMaxPlayers={lobby.players >= lobby.maxPlayers}
                  onClick={() => {
                    if (!(lobby.players >= lobby.maxPlayers))
                      connectRoom(lobby.name);
                  }}
                >
                  <LobbyName>{lobby.name}</LobbyName>
                  <span>
                    {lobby.players >= lobby.maxPlayers
                      ? "max"
                      : `${lobby.players}/${lobby.maxPlayers}`}
                  </span>
                  <LobbyDescription>{lobby.description}</LobbyDescription>
                  <span>{lobby.createdBy}</span>
                </LobbyContainer>
              ))}
            {!lobbiesNumber && <>{lobbiesNumber} lobbies found</>}
          </LobbiesWrapper>
          <LobbiesBtns>
            <button onClick={getRooms}>refresh</button>
            <button onClick={openModal}>create room</button>
          </LobbiesBtns>
        </Lobbies>
      )}
      {!fetched && (
        <>
          Fetching lobbies
          <Loader />
        </>
      )}
      {renderModal(
        <Modal>
          <ModalTitle>Create Room</ModalTitle>
          <ModalForm onSubmit={createRoom}>
            <input name="name" type="text" placeholder="room name" required />
            <input
              name="description"
              type="text"
              placeholder="room description"
              required
            />
            <input
              name="maxPlayers"
              type="number"
              min={2}
              max={5}
              placeholder="max players"
              required
            />
            <button type="submit">create</button>
          </ModalForm>
        </Modal>
      )}
    </GeneralWrapper>
  );
};
