import styled from "styled-components";

export const GeneralWrapper = styled.div`
  position: absolute;
  width: 768px;
  top: calc(50% + 75px);
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 10px;
  background-color: aliceblue;
  border: 1px solid red;
  border-radius: 4px;
`;

export const Container = styled.div`
  display: grid;
  grid-template-columns: 1fr 3fr;
  grid-template-rows: 300px;
  gap: 15px;
`;

export const RoomButtonsWrapper = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 20px;
`;
