<template>
  <div v-loading="loading"
       class="main-container">
    <filtrate-handle-view class="filtrate-bar"
                          moduleType="business"
                          :showBusinessSelect="true"
                          @load="loading=true"
                          @change="getDataList">
    </filtrate-handle-view>
    <div class="content">
      <div class="axis-content">
        <div id="axismain"></div>
      </div>
      <div class="table-content">
        <el-table :data="list"
                  height="400"
                  stripe
                  border
                  highlight-current-row>
          <el-table-column v-for="(item, index) in fieldList"
                           :key="index"
                           align="center"
                           header-align="center"
                           show-overflow-tooltip
                           :prop="item.field"
                           :label="item.name">
          </el-table-column>
        </el-table>
      </div>
    </div>

  </div>
</template>

<script>
import base from '../mixins/base'
import echarts from 'echarts'
import { biBusinessFunnel } from '@/api/businessIntelligence/bi'

export default {
  /** 销售漏斗 */
  name: 'funnel-statistics',
  components: {},
  data() {
    return {
      loading: false,

      list: [],
      fieldList: [
        { field: 'name', name: '阶段' },
        { field: 'totalPrice', name: '金额' },
        { field: 'businessNum', name: '商机数' }
      ],

      funnelChart: null, // 漏斗图
      funnelOption: null
    }
  },
  mixins: [base],
  computed: {},
  mounted() {
    this.initAxis()
  },
  methods: {
    /**
     * 图表数据
     */
    getDataList(params) {
      this.loading = true
      biBusinessFunnel(params)
        .then(res => {
          this.loading = false
          this.list = res.data
          var data = []
          let sumMoney = 0
          for (let index = 0; index < res.data.length; index++) {
            const element = res.data[index]
            data.push({
              name: (element.name || '') + '(' + element.businessNum + ')',
              value: parseFloat(element.totalPrice)
            })
            sumMoney += parseFloat(element.totalPrice)
          }

          this.funnelOption.series[0].data = data
          this.funnelOption.series[0].max = sumMoney < 1 ? 1 : sumMoney
          this.funnelChart.setOption(this.funnelOption, true)
        })
        .catch(() => {
          this.loading = false
        })
    },
    /** 销售漏斗 */
    initAxis() {
      var funnelChart = echarts.init(document.getElementById('axismain'))
      var option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b} <br/> 预测金额: {c}元'
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        calculable: true,
        grid: {
          left: 0,
          right: 0,
          bottom: 0,
          top: 0
        },
        color: this.chartColors,
        series: [
          {
            name: '漏斗图',
            type: 'funnel',
            left: '20%',
            width: '56%',
            /** 数据排序 */
            sort: 'none',
            /** 数据图形间距。 */
            gap: 2,

            label: {
              normal: {
                show: true,
                position: 'right'
              },
              emphasis: {
                textStyle: {
                  fontSize: 20
                }
              }
            },
            labelLine: {
              normal: {
                length: 20,
                lineStyle: {
                  width: 1,
                  type: 'solid'
                }
              }
            },
            data: []
          }
        ]
      }

      funnelChart.setOption(option, true)
      this.funnelOption = option
      this.funnelChart = funnelChart
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import '../styles/detail.scss';
</style>
