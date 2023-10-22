package com.calvin.trawlbenstechtest.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.calvin.trawlbenstechtest.domain.uimodel.AnimeCardUiModel
import com.calvin.trawlbenstechtest.domain.uimodel.PagingDataUiModel

class AnimePagingSource(private val onResponse: suspend (LoadParams<Int>) -> PagingDataUiModel) :
    PagingSource<Int, AnimeCardUiModel>() {
    private val initialPageIndex: Int by lazy { 1 }

    override fun getRefreshKey(state: PagingState<Int, AnimeCardUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeCardUiModel> {
        return try {
            val position = params.key ?: initialPageIndex
            val response = onResponse.invoke(params)

            if(response.data.isNotEmpty()) {
                LoadResult.Page(
                    data = response.data,
                    prevKey = if (position == initialPageIndex) null else position - 1,
                    nextKey = if (response.hasNextPage == false) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Something Went Wrong"))
            }
        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        }
    }
}