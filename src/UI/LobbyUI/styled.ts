import styled from "styled-components";
import { Wrapper } from "UI/common";

export const GeneralWrapper = styled(Wrapper)`
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
`;

export const Lobbies = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
  gap: 20px;
`;

export const LobbiesWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
`;

export const LobbyContainer = styled.button<{ $isMaxPlayers: boolean }>`
  display: block;
  width: 230px;
  padding: 5px;
  border: 1px solid red;
  border-radius: 4px;
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-template-rows: 1fr 1fr;

  text-align: start;

  cursor: pointer;
  background-color: transparent;
  ${({ $isMaxPlayers }) =>
    $isMaxPlayers && {
      cursor: "default",
      backgroundColor: "red",
    }}
  span {
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  span:nth-child(2n) {
    text-align: end;
  }
`;

const OverflowHidden = styled.span`
  overflow-x: hidden;
  text-overflow: ellipsis;
`;

export const LobbyName = styled(OverflowHidden)`
  max-width: 145px;
`;

export const LobbyDescription = styled(OverflowHidden)`
  text-align: start;
`;

export const Loader = styled.div`
  border: 3px solid red;
  border-radius: 100%;
  padding: 10px;
  width: 40px;
  height: 40px;
  border-bottom-width: 0px;
  border-right-width: 0px;

  animation: rotateLoader 1s linear infinite;

  @keyframes rotateLoader {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
`;

export const Modal = styled.div`
  background-color: aliceblue;
  padding: 10px;
  border: 1px solid red;
  border-radius: 5px;
`;

export const ModalTitle = styled.h2`
  text-align: center;
`;

export const ModalForm = styled.form`
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8px;
`;

export const LobbiesBtns = styled.div`
  display: flex;
  gap: 8px;
`;
