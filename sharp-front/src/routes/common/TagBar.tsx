import { ActionIcon, Badge, Select, SelectItem } from '@mantine/core';
import { IconTags } from '@tabler/icons-react';
import React from 'react';
import { SimpleTagView, TaggedType, getResourceTags, searchTags, doTag, createTag } from '../../apis/tag';
import { toastError } from '../../error';
import _ from 'lodash';

import AuthHelper from './AuthHelper';

type TagSearchProps = {
  onSelect: (tag: SimpleTagView) => void;
};

type TagSearchState = {
  tagName2Id: Record<string, number>;
  searchValue: string;
  value: string | null;
};

class TagSearch extends React.Component<TagSearchProps, TagSearchState> {
  state: Readonly<TagSearchState> = {
    tagName2Id: {},
    searchValue: '',
    value: null,
  };

  handleSearch = (q: string) => {
    const { tagName2Id } = this.state;
    if (!(q in tagName2Id)) {
      searchTags(q)
        .then((tags) => {
          const map = { ...tagName2Id };
          tags.forEach((t) => {
            map[t.name] = t.id;
          });
          this.setState({ tagName2Id: map });
        })
        .catch((err) => toastError(err, '搜索标签失败'));
    }
    this.setState({ searchValue: q });
  };

  render(): React.ReactNode {
    const { tagName2Id, searchValue, value } = this.state;
    const { onSelect } = this.props;

    const data: SelectItem[] = [];
    for (const name in tagName2Id) {
      data.push({ value: name, label: name });
    }

    return (
      <Select
        label="搜索/选择标签"
        data={data}
        placeholder="搜索/选择标签"
        nothingFound="无标签"
        searchable
        size="xs"
        width={180}
        searchValue={searchValue}
        onSearchChange={this.handleSearch}
        value={value}
        onChange={(v) => this.setState({ value: v })}
        onKeyUp={({ key }) => {
          if (key === 'Enter' && value) {
            onSelect({ id: tagName2Id[value], name: value });
          }
        }}
        getCreateLabel={(query) => (
          <span>
            + 创建新标签：「<strong>{query}</strong>」
          </span>
        )}
        creatable
        onCreate={(query) => {
          createTag(query)
            .then((tag) => {
              this.setState({ tagName2Id: { ...tagName2Id, [tag.name]: tag.id } });
            })
            .catch((err) => toastError(err, '创建标签失败'));
          return { value: query, label: query };
        }}
      />
    );
  }
}

type TagBarProps = {
  type: TaggedType;
  id: string;
};

type TagBar0States = {
  tags: SimpleTagView[];
  showSearch: boolean;
};

class TagBar0 extends React.Component<TagBarProps & { authed: boolean }, TagBar0States> {
  state: Readonly<TagBar0States> = {
    tags: [],
    showSearch: false,
  };

  componentDidMount(): void {
    const { type, id } = this.props;
    getResourceTags(type, id)
      .then((tags) => this.setState({ tags }))
      .catch((err) => toastError(err, '获取标签失败'));
  }

  // TODO: 删除标签
  render(): React.ReactNode {
    const { authed, type, id } = this.props;
    const { tags, showSearch } = this.state;
    return (
      <div>
        {authed &&
          (showSearch ? (
            <TagSearch
              onSelect={(value) => {
                this.setState({ showSearch: false });
                doTag(type, id, { added: [value.id] })
                  .then((addedTags) => {
                    this.setState({ tags: _.unionBy([...tags, ...addedTags], (it) => it.id) });
                  })
                  .catch((err) => toastError(err, '打标签失败'));
              }}
            />
          ) : (
            <ActionIcon onClick={() => this.setState({ showSearch: true })}>
              <IconTags />
            </ActionIcon>
          ))}
        {tags.map((it) => (
          <Badge key={it.id}>{it.name}</Badge>
        ))}
      </div>
    );
  }
}

export default function TagBar({ type, id }: TagBarProps) {
  return (
    <AuthHelper
      onAuth={() => <TagBar0 type={type} id={id} authed />}
      onUnauth={() => <TagBar0 type={type} id={id} authed={false} />}
      onLoading={() => <TagBar0 type={type} id={id} authed={false} />}
    />
  );
}
