import styled from "styled-components";

export const ConsoleWrapper = styled.div`
  position: absolute;
  max-width: 300px;
  right: 10px;
  top: 10px;
  border: 1px solid #03d203;
  border-radius: 10px;
  padding: 10px;
  background-color: #0e0e0e;
  color: #03d203;
  display: flex;
  flex-direction: column;
  gap: 5px;
`;

export const Block = styled.div`
  border: 1px solid #03d203;
  border-radius: 10px;
  padding: 10px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
`;

export const Title = styled.h2`
  margin-bottom: 10px;
`;
