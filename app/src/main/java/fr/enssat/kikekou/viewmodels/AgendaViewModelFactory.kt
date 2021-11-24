package fr.enssat.kikekou.viewmodels
/*
 * Copyright (C) 2019 - 2020 Orange
 *
 *  This software is confidential and proprietary information of Orange.
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into.
 *  Unauthorized copying of this file, via any medium is strictly prohibited.
 *
 *  If you are Orange employee you shall use this software in accordance with
 *  the Orange Source Charter ( http://opensource.itn.ftgroup/index.php/Orange_Source.
 */
/**
 * Project:    AndroidWasSdk
 * Created:    27/12/19
 * Created by: Gilles Le Brun
 *
 * @version *Version*
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.enssat.kikekou.room.AgendaRepository

class AgendaViewModelFactory(private val repository: AgendaRepository): ViewModelProvider.Factory {
   @Suppress("UNCHECKED_CAST")
    //factory created to pass repository to view model...
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AgendaViewModel(repository) as T
    }
 }
