import styled from "styled-components";

export const Wrapper = styled.div`
  position: absolute;
  max-width: 768px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 10px;
  background-color: aliceblue;
  border: 1px solid red;
  border-radius: 4px;
`;
export const BorderWrapper = styled.div`
  padding: 10px;
  background-color: aliceblue;
  border: 1px solid red;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
`;
