CREATE TABLE users_subscriptions (
     user_id UUID NOT NULL references users(id),
     subscription_id UUID NOT NULL references subscriptions(id),
     expiration_date DATE NOT NULL,
     active BOOLEAN NOT NULL,
     created_at DATE NOT NULL,
     updated_at DATE NOT NULL,
     PRIMARY KEY (user_id, subscription_id)
);

CREATE INDEX idx_users_subscriptions_subscription_id
    ON users_subscriptions (subscription_id);

ALTER TABLE users_subscriptions
DROP CONSTRAINT users_subscriptions_user_id_fkey,
    ADD CONSTRAINT users_subscriptions_user_id_fkey
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE;

ALTER TABLE users_subscriptions
DROP CONSTRAINT users_subscriptions_subscription_id_fkey,
    ADD CONSTRAINT users_subscriptions_subscription_id_fkey
        FOREIGN KEY (subscription_id)
        REFERENCES subscriptions(id)
        ON DELETE CASCADE;