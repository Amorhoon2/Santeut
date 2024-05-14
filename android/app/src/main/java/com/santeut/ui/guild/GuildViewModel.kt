package com.santeut.ui.guild

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santeut.data.model.request.CreateGuildPostRequest
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.RankingResponse
import com.santeut.domain.usecase.GuildUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GuildViewModel @Inject constructor(
    private val guildUseCase: GuildUseCase
) : ViewModel() {

    private val _guilds = MutableLiveData<List<GuildResponse>>(emptyList())
    val guilds: LiveData<List<GuildResponse>> = _guilds

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _guild = MutableLiveData<GuildResponse>()
    val guild: LiveData<GuildResponse> = _guild

    private val _postList = MutableLiveData<List<GuildPostResponse>>(emptyList())
    val postList: LiveData<List<GuildPostResponse>> = _postList

    private val _post = MutableLiveData<GuildPostDetailResponse>()
    val post: LiveData<GuildPostDetailResponse> = _post

    private val _memberList = MutableLiveData<List<GuildMemberResponse>>(emptyList())
    val memberList: LiveData<List<GuildMemberResponse>> = _memberList

    private val _rankingList = MutableLiveData<List<RankingResponse>>(emptyList())
    val rankingList: LiveData<List<RankingResponse>> = _rankingList

    fun getGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.getGuilds())
            } catch (e: Exception) {
                _error.postValue("동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun myGuilds() {
        viewModelScope.launch {
            try {
                _guilds.postValue(guildUseCase.myGuilds())
            } catch (e: Exception) {
                _error.postValue("내 동호회 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                _guild.value = guildUseCase.getGuild(guildId)
            } catch (e: Exception) {
                _error.postValue("동호회 정보 조회 실패: ${e.message}")
            }
        }
    }

    fun applyGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.applyGuild(guildId).collect {
                    Log.d("GuildViewModel", "가입 요청 전송")
                }
            } catch (e: Exception) {
                _error.postValue("가입 요청 전송 실패: ${e.message}")
            }
        }
    }

    fun getGuildPostList(guildId: Int, categoryId: Int) {
        viewModelScope.launch {
            try {
                _postList.postValue(guildUseCase.getGuildPostList(guildId, categoryId))
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 목록 조회 실패: ${e.message}")
            }
        }
    }

    fun createGuildPost(
        images: List<MultipartBody.Part>?,
        createGuildPostRequest: CreateGuildPostRequest
    ) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "접근 성공")
                guildUseCase.createGuildPost(images, createGuildPostRequest).collect {
                    Log.d("GuildViewModel", "동호회 게시글 작성 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 게시글 작성 실패: ${e.message}")
            }
        }
    }

    fun getGuildPost(guildPostId: Int) {
        viewModelScope.launch {
            try {
                _post.postValue(guildUseCase.getGuildPost(guildPostId))
            } catch (e: Exception) {
                _error.postValue("게시글 조회 실패: ${e.message}")
            }
        }
    }

    fun getGuildMemberList(guildId: Int) {
        viewModelScope.launch {
            try {
                _memberList.postValue(guildUseCase.getGuildMemberList(guildId))
            } catch (e: Exception) {
                _error.postValue("동호회 회원 조회 실패: ${e.message}")
            }
        }
    }

    fun exileMember(guildId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.exileMember(guildId, userId).collect {
                    Log.d("GuildViewModel", "회원 추방 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("회원 추방 요청 실패: ${e.message}")
            }
        }
    }

    fun changeLeader(guildId: Int, newLeaderId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.changeLeader(guildId, newLeaderId).collect {
                    Log.d("GuildViewModel", "회장 위임 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("회장 위임 요청 실패: ${e.message}")
            }
        }
    }

    fun quitGuild(guildId: Int) {
        viewModelScope.launch {
            try {
                guildUseCase.quitGuild(guildId).collect {
                    Log.d("GuildViewModel", "동호회 탈퇴 요청 성공")
                }
            } catch (e: Exception) {
                _error.postValue("동호회 탈퇴 요청 실패: ${e.message}")
            }
        }
    }

    fun getRanking(type: Char) {
        viewModelScope.launch {
            try {
                _rankingList.postValue(guildUseCase.getRanking(type))
            } catch (e: Exception) {
                _error.postValue("랭킹 조회 실패: ${e.message}")
            }
        }
    }

}