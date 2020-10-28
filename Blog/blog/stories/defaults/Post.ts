import { IComment, IPost } from 'types/Post';

export const DEFAULT_COMMENT: IComment = {
  author: '1234',
  text: 'First Comment',
  children: [],
  likes: ['2345'],
  createdAt: new Date(1602288000000),
  id: '78901',
};

export const DEFAULT_POST: IPost = {
  title: 'First Post',
  thumbnail:
    'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Desktop_computer_clipart_-_Yellow_theme.svg/1200px-Desktop_computer_clipart_-_Yellow_theme.svg.png',
  body: '# Lorem Ipsum\nBuy our product.',
  summary: 'Definitely not a marketing ploy.',
  likes: ['1234'],
  comments: ['78901'],
  author: '2345',
  createdAt: new Date(1602201600000),
  id: '567890',
};
