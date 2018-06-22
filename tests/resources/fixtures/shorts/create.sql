Create table V_PurchaseReportWithExpiry_NEW (
        Item VARCHAR(50) NOT NULL,
        ShelfLife INTEGER NOT NULL,
        Is_Atp INTEGER NOT NULL,
        long_description VARCHAR(50) NOT NULL,
        QtyPendingPutaway INTEGER NOT NULL,
        QtyInPool INTEGER NOT NULL,
        ActualOnHandQty INTEGER NOT NULL,
        customer_expiry TIMESTAMP NOT NULL,
        ExpDate TIMESTAMP,
        warehouse VARCHAR(50) NOT NULL
)