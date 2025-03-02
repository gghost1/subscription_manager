CREATE TABLE users_subscriptions (
     user_id UUID NOT NULL references users(id),
     subscription_id UUID NOT NULL references subscriptions(id),
     expiration_date DATE NOT NULL,
     active BOOLEAN NOT NULL,
     PRIMARY KEY (user_id, subscription_id)
);

CREATE INDEX idx_users_subscriptions_subscription_id
    ON users_subscriptions (subscription_id);