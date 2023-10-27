CREATE TABLE LINE_ITEMS (
    LINE_ID NUMBER(5, 0) DEFAULT LINE_ITEMS_ID_GENERATOR.NEXTVAL,
    QUANTITY NUMBER(4, 0) DEFAULT 0 NOT NULL,
    ITEM_ID NUMBER(5, 0),
    ORDER_ID NUMBER(5, 0),
    CONSTRAINT LINE_PK PRIMARY KEY (LINE_ID),
    CONSTRAINT LINE_ITEM_FK FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ITEM_ID),
    CONSTRAINT LINE_ORDER_FK FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID)
);