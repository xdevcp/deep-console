import React from 'react';
import PropTypes from 'prop-types';
import {
  Button,
  Field,
  Form,
  Grid,
  Input,
  Loading,
  Pagination,
  Table,
  Dialog,
  Message,
  ConfigProvider,
  Switch,
} from '@alifd/next';
import { request } from '../../../globalLib';
import EditDataDictDialog from '../Detail/EditDialog';

import './list.scss';

const FormItem = Form.Item;
const { Row, Col } = Grid;
const { Column } = Table;
const configsTableSelected = new Map();

@ConfigProvider.config
class DataDictList extends React.Component {
  static displayName = 'DataDictList';

  static propTypes = {
    locale: PropTypes.object,
    history: PropTypes.object,
  };

  constructor(props) {
    super(props);
    this.editDialog = React.createRef();
    this.state = {
      loading: false,
      total: 0,
      pageSize: 10,
      currentPage: 1,
      dataSource: [],
      search: {
        dataCode: '',
        dataType: '',
      },
      isCheckAll: false,
      rowSelection: {
        onChange: this.configDataTableOnChange.bind(this),
        selectedRowKeys: [],
      },
    };
    this.field = new Field(this);
  }

  componentDidMount() {
    this.queryList(0);
  }

  openLoading() {
    this.setState({ loading: true });
  }

  closeLoading() {
    this.setState({ loading: false });
  }

  openEditDataDictDialog() {
    try {
      this.editDialog.current.getInstance().show(this.state.source);
    } catch (error) {}
  }

  queryList(delayTime = 2000) {
    const self = this;
    const { search, currentPage, pageSize, parentId } = self.state;
    const parameter = [`pageNo=${currentPage}`, `pageSize=${pageSize}`, `parentId=${parentId}`];
    // console.log(111);
    setTimeout(() => {
      request({
        url: `v1/open/dictionary?${parameter.join('&')}`,
        beforeSend: () => self.openLoading(),
        success: res => {
          if (res.success === true) {
            const count = res.data.total;
            const dataList = res.data.list || [];
            self.setState({
              dataSource: dataList,
              total: count,
            });
          } else {
            Dialog.alert({
              title: prompt,
              content: res.message,
            });
          }
        },
        error: () =>
          self.setState({
            dataSource: [],
            total: 0,
            currentPage: 0,
          }),
        complete: () => self.closeLoading(),
      });
    }, delayTime);
  }

  getQueryLater = () => {
    setTimeout(() => this.queryList());
  };

  deleteOperation(source) {
    const { locale = {} } = this.props;
    const { prompt, promptDelete } = locale;
    Dialog.confirm({
      title: prompt,
      content: promptDelete,
      onOk: () => {
        request({
          method: 'DELETE',
          url: `v1/ns/service?dataCode=${source.name}&dataType=${source.dataType}`,
          dataType: 'text',
          beforeSend: () => this.openLoading(),
          success: res => {
            if (res !== 'ok') {
              Message.error(res);
              return;
            }
            this.queryList();
          },
          error: res => Message.error(res.responseText || res.statusText),
          complete: () => this.closeLoading(),
        });
      },
    });
  }

  configDataTableOnChange(ids, records) {
    const { rowSelection } = this.state;
    rowSelection.selectedRowKeys = ids;
    this.setState({ rowSelection });
    configsTableSelected.clear();
    records.forEach((record, i) => {
      configsTableSelected.set(record.id, record);
    });
  }

  render() {
    const { locale = {} } = this.props;
    const {
      pubNoData,
      dataCode,
      dataCodePlaceholder,
      dataType,
      dataTypePlaceholder,
      query,
      create,
      operation,
      detail,
      deleteAction,
    } = locale;
    const { search } = this.state;
    const { init, getValue } = this.field;
    this.init = init;
    this.getValue = getValue;

    return (
      <div className="main-container general-class">
        <Loading
          shape="flower"
          style={{
            position: 'relative',
            width: '100%',
          }}
          visible={this.state.loading}
          tip="Loading..."
        >
          <h3
            style={{
              height: 30,
              width: '100%',
              lineHeight: '30px',
              padding: 0,
              margin: 0,
              paddingLeft: 10,
              borderLeft: '3px solid #09c',
              color: '#ccc',
              fontSize: '12px',
            }}
          >
            <span style={{ fontSize: '14px', color: '#000', marginRight: 8 }}>数据字典</span>
            {locale.queryResults}
            <strong style={{ fontWeight: 'bold' }}> {this.state.total} </strong>
            条命中
          </h3>
          <Row
            className="demo-row"
            style={{
              marginBottom: 10,
              padding: 0,
            }}
          >
            <Col span="24">
              <Form inline field={this.field}>
                <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <FormItem label={dataCode}>
                  <Input
                    placeholder={dataCodePlaceholder}
                    style={{ width: 200 }}
                    value={search.dataCode}
                    onChange={dataCode => this.setState({ search: { ...search, dataCode } })}
                    onPressEnter={() => this.setState({ currentPage: 1 }, () => this.queryList())}
                  />
                </FormItem>
                <FormItem label={dataType}>
                  <Input
                    placeholder={dataTypePlaceholder}
                    style={{ width: 200 }}
                    value={search.dataType}
                    onChange={dataType => this.setState({ search: { ...search, dataType } })}
                    onPressEnter={() => this.setState({ currentPage: 1 }, () => this.queryList())}
                  />
                </FormItem>
                <FormItem label="">
                  <Button
                    type="primary"
                    onClick={() => this.setState({ currentPage: 1 }, () => this.queryList())}
                    style={{ marginRight: 10 }}
                  >
                    {query}
                  </Button>
                </FormItem>
                <FormItem label="" style={{ float: 'right' }}>
                  <Button type="secondary" onClick={() => this.openEditDataDictDialog()}>
                    {create}
                  </Button>
                </FormItem>
              </Form>
            </Col>
          </Row>
          <Row style={{ padding: 0 }}>
            <Col span="24" style={{ padding: 0 }}>
              <Table
                dataSource={this.state.dataSource}
                locale={{ empty: pubNoData }}
                fixedHeader
                maxBodyHeight={400}
                ref={'dataTable'}
                rowSelection={this.state.rowSelection}
              >
                <Column title={locale.field1} dataIndex="dataType" />
                <Column title={locale.field2} dataIndex="dataCode" />
                <Column title={locale.field3} dataIndex="dataValue" />
                <Column title={locale.field4} dataIndex="sortNo" />
                <Column title={locale.field5} dataIndex="status" />
                <Column title={locale.field6} dataIndex="updateTime" width={200} />
                <Column
                  title={operation}
                  align="center"
                  cell={(value, index, record) => (
                    // @author deep
                    /* Add a link to view "sample code"
                     replace the original button with a label,
                     which is consistent with the operation style in configuration management.
                     */
                    <div>
                      <a
                        onClick={() =>
                          this.props.history.push(
                            `/serviceDetail?name=${record.name}&groupName=${record.groupName}`
                          )
                        }
                        style={{ marginRight: 5 }}
                      >
                        {detail}
                      </a>
                      <span style={{ marginRight: 5 }}>|</span>
                      <a onClick={() => this.deleteOperation(record)} style={{ marginRight: 5 }}>
                        {deleteAction}
                      </a>
                    </div>
                  )}
                />
              </Table>
            </Col>
          </Row>
          {this.state.total > this.state.pageSize && (
            <div
              style={{
                marginTop: 10,
                textAlign: 'right',
              }}
            >
              <Pagination
                current={this.state.currentPage}
                total={this.state.total}
                pageSize={this.state.pageSize}
                onChange={currentPage => this.setState({ currentPage }, () => this.queryList())}
              />
            </div>
          )}
        </Loading>
        <EditDataDictDialog
          ref={this.editDialog}
          openLoading={() => this.openLoading()}
          closeLoading={() => this.closeLoading()}
          queryList={() => this.setState({ currentPage: 1 }, () => this.queryList())}
        />
      </div>
    );
  }
}

export default DataDictList;
