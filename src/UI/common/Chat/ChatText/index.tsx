import { useEffect, useRef, useState } from "react";
import { Container, MessageContainer } from "./styled";
import { GameUI } from "Types/game";

type ChatMessage = [UserName: string | null, Message: string];

export const ChatText: GameUI = ({ game }) => {
  const [chatMessage, setChatMessage] = useState<ChatMessage[]>([
    [null, "Welcome!"],
  ]);
  const containerRef = useRef<HTMLDivElement>(null);

  const addChatMessage = (message: ChatMessage) =>
    setChatMessage((prev) => prev.concat([message]));

  useEffect(() => {
    const onGetUserCallback = (response: string[]) => {
      const userNames = response;
      const lastUsername = userNames.at(-1);
      const currentUsername = game.game?.registry.get("username");

      if (lastUsername !== currentUsername)
        addChatMessage([null, `${lastUsername} is connected`]);
    };
    game.scene?.addSocketListener("roomGetUsersServer", onGetUserCallback);

    const onRoomMessageCallback = (response: string) => {
      const chatMessage = JSON.parse(response) as ChatMessage;
      addChatMessage(chatMessage);
    };
    game.scene?.addSocketListener(
      "roomMessageSendServer",
      onRoomMessageCallback
    );

    return () => {
      game.scene?.removeSocketListener("roomGetUsersServer", onGetUserCallback);
      game.scene?.removeSocketListener(
        "roomMessageSendServer",
        onRoomMessageCallback
      );
    };
  }, []);

  useEffect(() => {
    containerRef.current?.scrollTo(0, containerRef.current.scrollHeight);
  }, [chatMessage]);

  return (
    <Container ref={containerRef}>
      {chatMessage.map(([username, messgae], index) => (
        <MessageContainer $isSystem={username === null} key={index}>
          {username && <span>{username}:</span>}
          <span>{messgae}</span>
        </MessageContainer>
      ))}
    </Container>
  );
};
