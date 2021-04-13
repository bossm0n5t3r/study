package com.example.demospringdatajpa.domain.board.type

import org.springframework.data.jpa.repository.JpaRepository

interface BoardTypeRepository : JpaRepository<BoardType, Int>
