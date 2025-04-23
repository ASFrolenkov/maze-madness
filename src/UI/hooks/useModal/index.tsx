import { useCallback, useState } from "react";
import { createPortal } from "react-dom";
import { Background, ModalWrapper } from "./styled";

export const useModal = () => {
  const [showModal, setShowModal] = useState(false);

  const openModal = () => setShowModal(true);

  const closeModal = () => setShowModal(false);

  const renderModal = useCallback(
    (Component: JSX.Element) => {
      const app = document.querySelector("#app");
      if (showModal && app)
        return createPortal(
          <Background onClick={closeModal}>
            <ModalWrapper onClick={(e) => e.stopPropagation()}>
              {Component}
            </ModalWrapper>
          </Background>,
          app
        );
      return <></>;
    },
    [showModal]
  );

  return { renderModal, openModal, closeModal };
};
