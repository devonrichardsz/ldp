import { IUser } from 'types/User';

export const DEFAULT_USER = {
  id: '1234',
  name: 'Test User 1',
  avatarUrl: 'https://miro.medium.com/fit/c/128/128/1*xvANltgt-vBgdEIDmkt-XA.jpeg',
  about: 'I was the first test user created.',
  following: ['2345'],
};

export const DEFAULT_USER_NO_AVATAR = {
  id: '2345',
  name: 'Test User 2',
  about: 'I was the second test user created, but the developer secretly loves me more.',
  following: ['1234'],
};
