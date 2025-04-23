import styled, { css } from "styled-components";

export const Container = styled.div`
  width: 100%;
  height: 100%;
  background-color: #c0ddff;
  padding: 8px;
  overflow-y: scroll;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export const MessageContainer = styled.span<{ $isSystem: boolean }>`
  display: flex;
  gap: 6px;
  ${({ $isSystem }) =>
    $isSystem &&
    css`
      color: gray;
    `}
`;
