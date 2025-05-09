-- Create store table
CREATE TABLE workschd.store
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_active        BIT                                    NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by       VARCHAR(255) DEFAULT 'SYSTEM'          NULL,
    last_modified_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255)                           NULL,
    address          VARCHAR(255)                           NULL,
    branch_id        BIGINT                                 NULL,
    name             VARCHAR(255)                           NOT NULL,
    region           VARCHAR(255)                           NULL
);

-- Insert sample data
INSERT INTO workschd.store (is_active, address, branch_id, name, region) VALUES
(1, '123 Gangnam-daero, Gangnam-gu, Seoul', 1, 'Gangnam Store', 'Seoul'),
(1, '456 Myeongdong-gil, Jung-gu, Seoul', 2, 'Myeongdong Store', 'Seoul'),
(1, '789 Hongdae-ro, Mapo-gu, Seoul', 3, 'Hongdae Store', 'Seoul'),
(1, '321 Haeundae-ro, Haeundae-gu, Busan', 4, 'Haeundae Store', 'Busan'),
(1, '654 Seomyeon-ro, Busanjin-gu, Busan', 5, 'Seomyeon Store', 'Busan'),
(1, '987 Jungang-ro, Dong-gu, Daegu', 6, 'Daegu Central Store', 'Daegu'),
(1, '147 Dongseong-ro, Jung-gu, Daegu', 7, 'Dongseong Store', 'Daegu'),
(1, '258 Gwangbok-ro, Jung-gu, Busan', 8, 'Gwangbok Store', 'Busan'),
(1, '369 Sejong-daero, Jung-gu, Seoul', 9, 'Sejong Store', 'Seoul'),
(1, '741 Gwangan-ro, Suyeong-gu, Busan', 10, 'Gwangan Store', 'Busan'),
(1, '852 Dongdaemun-ro, Jongno-gu, Seoul', 11, 'Dongdaemun Store', 'Seoul'),
(1, '963 Bujeon-ro, Busanjin-gu, Busan', 12, 'Bujeon Store', 'Busan'),
(1, '159 Jungang-daero, Jung-gu, Incheon', 13, 'Incheon Central Store', 'Incheon'),
(1, '753 Sinchon-ro, Seodaemun-gu, Seoul', 14, 'Sinchon Store', 'Seoul'),
(1, '951 Dongbaek-ro, Haeundae-gu, Busan', 15, 'Dongbaek Store', 'Busan'); 