import React from 'react';

import { Markdown, MarkdownProps } from 'components/atoms/Markdown';

export default {
  component: Markdown,
  title: 'Atoms/Markdown',
};

const Template = (args: MarkdownProps) => <Markdown {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  text: '# TITLE\nlorem ipsum',
};
