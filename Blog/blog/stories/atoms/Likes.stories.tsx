import React from 'react';

import { Likes, LikesProps } from 'components/atoms/Likes';

export default {
  component: Likes,
  title: 'Atoms/Likes',
};

const Template = (args: LikesProps) => <Likes {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  users: [],
};
