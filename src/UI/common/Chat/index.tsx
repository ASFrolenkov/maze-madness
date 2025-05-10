import { useState } from "react";
import { ChatInput, ChatWrapper } from "./styled";
import { GameUI } from "Types/game";
import { ChatText } from "./ChatText";

export const Chat: GameUI = ({ game }) => {
  const [inputValue, setInputValue] = useState("");

  const sendMessage = () => {
    const currentName = game.game?.registry.get("username");
    if (inputValue.trim())
      game.scene?.emitSocket(
        "roomMessageSendClient",
        JSON.stringify([currentName, inputValue])
      );
  };

  return (
    <ChatWrapper>
      <ChatText game={game} />
      <ChatInput
        placeholder="chat here"
        onChange={(e) => setInputValue(e.target.value)}
        value={inputValue}
        onKeyUp={(e) => {
          if (e.key === "Enter") {
            setInputValue("");
            sendMessage();
          }
        }}
      />
    </ChatWrapper>
  );
};
