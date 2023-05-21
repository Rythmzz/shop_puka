module.exports = ({ env }) => ({
  auth: {
    secret: env('ADMIN_JWT_SECRET', '25012001'),
  },
  apiToken: {
    salt: env('API_TOKEN_SALT','25012001'),
  },
  transfer: {
    token: {
      salt: env('TRANSFER_TOKEN_SALT'),
    },
  },
});
